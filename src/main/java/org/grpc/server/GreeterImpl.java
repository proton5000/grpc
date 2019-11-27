package org.grpc.server;

import io.grpc.stub.StreamObserver;
import org.grpc.GreeterGrpc;
import org.grpc.HelloReply;
import org.grpc.HelloRequest;

class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {

        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();

        responseObserver.onNext(reply);

        responseObserver.onCompleted();
    }
}
