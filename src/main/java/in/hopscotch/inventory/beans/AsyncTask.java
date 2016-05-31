package in.hopscotch.inventory.beans;

import java.util.TimerTask;

import org.springframework.web.context.request.async.DeferredResult;

public class AsyncTask extends TimerTask {
	private DeferredResult<String> deferredResult;
	
	public AsyncTask(DeferredResult<String> deferredResult) {
		this.deferredResult = deferredResult;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String res = "Hello Non Blocking!!!";
		deferredResult.setResult(res);
	}

}
