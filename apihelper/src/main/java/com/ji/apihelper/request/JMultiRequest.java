package com.ji.apihelper.request;

import com.ji.apihelper.entity.RequestOption;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

// support for java
public abstract class JMultiRequest<R> extends Request<Map<Class<?>, R>> {
    public JMultiRequest(@NotNull RequestOption option) {
        super(option);
    }

    public JMultiRequest() {
    }

    @NotNull
    @Override
    public Single<Map<Class<?>, R>> getApi() {
        switch (getType()) {
            case ZIP:
                return Single.zip(getApis(), objects -> {
                    Map<Class<?>, R> resultMap = new HashMap<>();
                    for (Object obj : objects) {
                        resultMap.put(obj.getClass(), (R) obj);
                    }
                    return resultMap;
                });

            case CONCAT:
                return Single.concat(getApis())
                        .toMap(R::getClass);
            case MERGE:
                return Single.merge(getApis())
                        .toMap(R::getClass);

            default:
                throw new IllegalStateException("Error request");

        }
    }

    public abstract List<Single<R>> getApis();

    public abstract MultiRequest.Type getType();
}
