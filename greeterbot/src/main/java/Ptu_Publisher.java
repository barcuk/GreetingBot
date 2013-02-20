
import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class Ptu_Publisher extends AbstractNodeMain {

    private Publisher<my_msgs.PtuInstruction> publisher;
    private my_msgs.PtuInstruction message;

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("PTUPublisher");
    }

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        publisher = connectedNode.newPublisher("Ptu_Instruction", my_msgs.PtuInstruction._TYPE);
        connectedNode.executeCancellableLoop(new CancellableLoop() {
            private int pan;
            private int tilt;

            @Override
            protected void setup() {
                pan = -130;
                tilt = 0;
            }

            @Override
            protected void loop() throws InterruptedException {
                System.out.println("Testing testing: " + pan);
                publishMessage(pan, tilt);
                if (pan <= 120) {
                    pan += 10;
                    
                } else {
                    throw new InterruptedException("Reached max pan");
                }
                if (tilt < 30) {
                    tilt -= 1;
                }
                Thread.sleep(1000);
            }
        });
    }

    public void publishMessage(float pan, float tilt) {
        message = publisher.newMessage();
        message.setPan(pan);
        message.setTilt(tilt);

        publisher.publish(message);
    }
}
