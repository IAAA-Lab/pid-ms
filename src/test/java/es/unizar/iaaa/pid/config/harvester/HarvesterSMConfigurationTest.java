package es.unizar.iaaa.pid.config.harvester;

import es.unizar.iaaa.pid.PidmsApp;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PidmsApp.class)
public class HarvesterSMConfigurationTest {

    @Autowired
    StateMachine<States,Events> stateMachine;

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void run() throws InterruptedException {
        stateMachine.start();
        Thread.sleep(10000);
        assertThat("The state must have no error", stateMachine.hasStateMachineError(), is(false));
        assertThat("The state must not be complete", stateMachine.isComplete(), is(false));
        stateMachine.sendEvent(Events.STOP);
        Thread.sleep(1000);
        assertThat("The state must be complete", stateMachine.isComplete(), is(true));
//        assertThat(capture.toString(), containsString(": Begin state machine"));
//        assertThat(capture.toString(), containsString(": Mark for validation"));
//        assertThat(capture.toString(), containsString(": Preparing harvest"));
//        assertThat(capture.toString(), containsString(": Harvest"));
//        assertThat(capture.toString(), containsString(": Transferring harvest"));
//        assertThat(capture.toString(), containsString(": Validation by id"));
//        assertThat(capture.toString(), containsString(": Transferring validation by id"));
//        assertThat(capture.toString(), containsString(": Finish validation"));
        assertThat(capture.toString(), containsString(": End state machine"));
    }

}
