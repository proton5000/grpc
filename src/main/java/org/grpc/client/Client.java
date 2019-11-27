package org.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.grpc.GreeterGrpc;
import org.grpc.HelloReply;
import org.grpc.HelloRequest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private final ManagedChannel channel;
    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private final GreeterGrpc.GreeterStub asyncStub;

    private final Logger logger = Logger.getLogger(Client.class.getName());

    public Client(String target) {
        channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build(); //remove .usePlainText for secure connections
        blockingStub = GreeterGrpc.newBlockingStub(channel); // use it to make blocking calls
        asyncStub = GreeterGrpc.newStub(channel); // or use this to make async calls
    }

    public void blockingGreet(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.withDeadlineAfter(2, TimeUnit.SECONDS).sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }

    public void asyncGreet(String name) {

        StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {

            public void onNext(HelloReply response) {
                logger.info("Greeting: " + response.getMessage());
            }

            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                logger.log(Level.WARNING, "RPC Failed: {0}", status);
            }

            public void onCompleted() {
                logger.info("Finished Greeting");
            }
        };

        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        asyncStub.withDeadlineAfter(2, TimeUnit.SECONDS).sayHello(request, responseObserver);
    }
}
