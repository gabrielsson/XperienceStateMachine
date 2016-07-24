package se.jeeves.xperiencestatemachine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Service;

/**
 * Created by max.gabrielsson on 24/07/16.
 */
public class ServiceHandler extends Handler {

    public ServiceHandler(Looper looper) {
        super(looper);
    }
    @Override
    public void handleMessage(Message msg) {


        Messenger messenger= MachineActivity.getMainActivity().mMessenger;

        try {
            Bundle data = msg.getData();
            int state = data.getInt("state");
            switch (state) {
                case MachineStates.RUN:
                    messenger.send(Message.obtain(null, MachineStates.RUN, "RUNNING"));
                    break;
                case MachineStates.STOP:
                    messenger.send(Message.obtain(null, MachineStates.STOP, "STOPPING"));
                    break;
                default:
                    break;


            }

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}