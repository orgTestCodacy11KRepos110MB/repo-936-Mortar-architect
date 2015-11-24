package architect.service.presentation;

import architect.service.Delegate;

/**
 * Created by lukasz on 23/11/15.
 */
public class ShowDelegate extends Delegate {

    @Override
    public boolean onBackPressed() {
        if (service.<ShowPresenter>getPresenter().onBackPressed()) {
            return true;
        }

        return service.getController().pop();
    }
}