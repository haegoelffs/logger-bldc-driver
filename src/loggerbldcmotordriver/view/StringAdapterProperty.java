package loggerbldcmotordriver.view;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author simon
 */
public class StringAdapterProperty extends StringProperty
{

    private final IPropertyData<String> data;

    private ObservableValue<? extends String> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<String> helper = null;

    public StringAdapterProperty(IPropertyData<String> data) {
        this.data = data;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void addListener(ChangeListener<? super String> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super String> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    /**
     * Sends notifications to all attached
     * {@link javafx.beans.InvalidationListener InvalidationListeners} and
     * {@link javafx.beans.value.ChangeListener ChangeListeners}.
     *
     * This method is called when the value is changed, either manually by
     * calling {@link #set} or in case of a bound property, if the binding
     * becomes invalid.
     */
    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    /**
     * The method {@code invalidated()} can be overridden to receive
     * invalidation notifications. This is the preferred option in
     * {@code Objects} defining the property, because it requires less memory.
     *
     * The default implementation is empty.
     */
    protected void invalidated() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get() {
        valid = true;
        return observable == null ? data.get() : observable.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String newValue) {
        if (isBound()) {
            throw new java.lang.RuntimeException((getBean() != null && getName() != null
                    ? getBean().getClass().getSimpleName() + "." + getName() + " : " : "") + "A bound value cannot be set.");
        }
        if ((data.get() == null) ? newValue != null : !data.get().equals(newValue)) {
            data.set(newValue);
            markInvalid();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBound() {
        return observable != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bind(ObservableValue<? extends String> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!newObservable.equals(observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new Listener(this);
            }
            observable.addListener(listener);
            markInvalid();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unbind() {
        if (observable != null) {
            data.set(observable.getValue());
            observable.removeListener(listener);
            observable = null;
        }
    }

    /**
     * Returns a string representation of this {@code StringPropertyBase}
     * object.
     *
     * @return a string representation of this {@code StringPropertyBase}
     * object.
     */
    @Override
    public String toString() {
        final Object bean = getBean();
        final String name = getName();
        final StringBuilder result = new StringBuilder("StringProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if ((name != null) && (!name.equals(""))) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (valid) {
                result.append("value: ").append(get());
            }
            else {
                result.append("invalid");
            }
        }
        else {
            result.append("value: ").append(get());
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public Object getBean() {
        return data.getBean();
    }

    @Override
    public String getName() {
        return data.getName();
    }

    private static class Listener implements InvalidationListener
    {

        private final WeakReference<StringAdapterProperty> wref;

        public Listener(StringAdapterProperty ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            StringAdapterProperty ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            }
            else {
                ref.markInvalid();
            }
        }
    }
}
