package araobp;

import org.slf4j.Logger;

import com.google.common.util.concurrent.FutureCallback;

public class LoggingFuturesCallBack<V> implements FutureCallback<V> {

    private Logger LOG;
    private String message;

    public LoggingFuturesCallBack(String message,Logger LOG) {
        this.message = message;
        this.LOG = LOG;
    }

    @Override
    public void onFailure(Throwable e) {
        LOG.warn(message, e);

    }

    @Override
    public void onSuccess(V arg0) {
        LOG.info("Success! {} ", arg0);

    }

}
