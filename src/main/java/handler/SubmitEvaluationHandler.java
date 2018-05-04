package handler;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;


import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Type;

public class SubmitEvaluationHandler extends JsonBaseHander<SubmitEvaluationHandler.Evaluation> {

    @Override
    protected void handleRequest(ChannelHandlerContext ctx, Evaluation t) {

        handleSuccessResponse(ctx,  "submit evaluation success");

    }

    @Override
    protected Type getRequestContentGsonType() {
        return new TypeToken<Evaluation>() {
        }.getType();
    }

    public static class Evaluation {
        public int star;
        public String content;
    }

}

