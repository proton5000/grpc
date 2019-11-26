package org.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.grpc.GrpcRequest;
import org.grpc.GrpcResponse;
import org.grpc.GrpcServiceGrpc;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        GrpcRequest grpcRequest = GrpcRequest.newBuilder().build();

        GrpcServiceGrpc.GrpcServiceBlockingStub stub = GrpcServiceGrpc.newBlockingStub(channel);

        GrpcResponse grpcResponse = stub.sendRpcRequest(
                GrpcRequest.newBuilder().setValue("1").build()
        );

        channel.shutdown();
    }
}
