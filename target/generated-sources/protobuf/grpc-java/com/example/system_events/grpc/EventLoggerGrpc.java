package com.example.system_events.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: system-events.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EventLoggerGrpc {

  private EventLoggerGrpc() {}

  public static final java.lang.String SERVICE_NAME = "EventLogger";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.example.system_events.grpc.EventRequest,
      com.example.system_events.grpc.EventResponse> getLogEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "LogEvent",
      requestType = com.example.system_events.grpc.EventRequest.class,
      responseType = com.example.system_events.grpc.EventResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.example.system_events.grpc.EventRequest,
      com.example.system_events.grpc.EventResponse> getLogEventMethod() {
    io.grpc.MethodDescriptor<com.example.system_events.grpc.EventRequest, com.example.system_events.grpc.EventResponse> getLogEventMethod;
    if ((getLogEventMethod = EventLoggerGrpc.getLogEventMethod) == null) {
      synchronized (EventLoggerGrpc.class) {
        if ((getLogEventMethod = EventLoggerGrpc.getLogEventMethod) == null) {
          EventLoggerGrpc.getLogEventMethod = getLogEventMethod =
              io.grpc.MethodDescriptor.<com.example.system_events.grpc.EventRequest, com.example.system_events.grpc.EventResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "LogEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.system_events.grpc.EventRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.example.system_events.grpc.EventResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventLoggerMethodDescriptorSupplier("LogEvent"))
              .build();
        }
      }
    }
    return getLogEventMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventLoggerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventLoggerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventLoggerStub>() {
        @java.lang.Override
        public EventLoggerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventLoggerStub(channel, callOptions);
        }
      };
    return EventLoggerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventLoggerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventLoggerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventLoggerBlockingStub>() {
        @java.lang.Override
        public EventLoggerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventLoggerBlockingStub(channel, callOptions);
        }
      };
    return EventLoggerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventLoggerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventLoggerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventLoggerFutureStub>() {
        @java.lang.Override
        public EventLoggerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventLoggerFutureStub(channel, callOptions);
        }
      };
    return EventLoggerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void logEvent(com.example.system_events.grpc.EventRequest request,
        io.grpc.stub.StreamObserver<com.example.system_events.grpc.EventResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLogEventMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EventLogger.
   */
  public static abstract class EventLoggerImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EventLoggerGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EventLogger.
   */
  public static final class EventLoggerStub
      extends io.grpc.stub.AbstractAsyncStub<EventLoggerStub> {
    private EventLoggerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventLoggerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventLoggerStub(channel, callOptions);
    }

    /**
     */
    public void logEvent(com.example.system_events.grpc.EventRequest request,
        io.grpc.stub.StreamObserver<com.example.system_events.grpc.EventResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLogEventMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EventLogger.
   */
  public static final class EventLoggerBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EventLoggerBlockingStub> {
    private EventLoggerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventLoggerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventLoggerBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.example.system_events.grpc.EventResponse logEvent(com.example.system_events.grpc.EventRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLogEventMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EventLogger.
   */
  public static final class EventLoggerFutureStub
      extends io.grpc.stub.AbstractFutureStub<EventLoggerFutureStub> {
    private EventLoggerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventLoggerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventLoggerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.example.system_events.grpc.EventResponse> logEvent(
        com.example.system_events.grpc.EventRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLogEventMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOG_EVENT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOG_EVENT:
          serviceImpl.logEvent((com.example.system_events.grpc.EventRequest) request,
              (io.grpc.stub.StreamObserver<com.example.system_events.grpc.EventResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLogEventMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.example.system_events.grpc.EventRequest,
              com.example.system_events.grpc.EventResponse>(
                service, METHODID_LOG_EVENT)))
        .build();
  }

  private static abstract class EventLoggerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventLoggerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.example.system_events.grpc.SystemEventsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EventLogger");
    }
  }

  private static final class EventLoggerFileDescriptorSupplier
      extends EventLoggerBaseDescriptorSupplier {
    EventLoggerFileDescriptorSupplier() {}
  }

  private static final class EventLoggerMethodDescriptorSupplier
      extends EventLoggerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EventLoggerMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EventLoggerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventLoggerFileDescriptorSupplier())
              .addMethod(getLogEventMethod())
              .build();
        }
      }
    }
    return result;
  }
}
