package entity;

import com.artemis.Component;
import com.artemis.annotations.DelayedComponentRemoval;

@DelayedComponentRemoval
public class Dialog extends Component {

    public static float DEFAULT_TIME = 7;
    public static float DEFAULT_ALPHA = 1;

    public String text;
    public float time = DEFAULT_TIME;
    public float alpha = DEFAULT_ALPHA;

    public Dialog() {
    }

    public Dialog(String text, float time, float alpha) {
        this.text = text;
        this.time = time;
        this.alpha = alpha;
    }
}
