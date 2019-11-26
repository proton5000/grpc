package org.grpc.server;

import io.grpc.stub.StreamObserver;
import org.grpc.GrpcRequest;
import org.grpc.GrpcResponse;
import org.grpc.GrpcServiceGrpc;

public class GrpcServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {

    @Override
    public void sendRpcRequest(
            GrpcRequest request, StreamObserver<GrpcResponse> responseObserver) {

        String greeting = new StringBuilder()
                .append(request.getValue())
                .toString();

        GrpcResponse response = GrpcResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
