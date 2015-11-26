package architect.examples.mortar_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import architect.Architect;
import architect.Attachments;
import architect.Stack;
import architect.examples.mortar_app.screen.home.HomeScreen;
import architect.examples.mortar_app.transition.BottomSlideTransition;
import architect.hook.mortar.MortarAchitect;
import architect.hook.mortar.MortarHook;
import architect.service.commons.FrameContainerView;
import architect.service.commons.Transitions;
import architect.service.show.ShowService;
import architect.service.show.Transition;
import architect.service.show.mortar.ShowServiceScopingStrategy;
import butterknife.Bind;
import butterknife.ButterKnife;
import mortar.MortarScope;

/**
 * Root activity
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class MainActivity extends AppCompatActivity {

    public static final String SHOW_SERVICE = "show";

    private Architect architect;

    @Bind(R.id.container_view)
    protected FrameContainerView containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MortarAchitect.onCreateScope(this, savedInstanceState, new MortarAchitect.Factory() {
            @Override
            public Architect createArchitect() {
                Architect architect = Architect.create(new Parceler());
                architect.register(SHOW_SERVICE, new ShowService() {
                    @Override
                    public void configureTransitions(Transitions<Transition> transitions) {
                        transitions.setDefault(new BottomSlideTransition());
                    }
                });
                return architect;
            }

            @Override
            public void configureArchitectWithMortar(Architect architect, MortarScope scope) {
                architect.addHook(new MortarHook(scope, new ShowServiceScopingStrategy(SHOW_SERVICE)));
            }

            @Override
            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {

            }
        });

        architect = MortarAchitect.get(this);
        architect.delegate().onCreate(getIntent(), savedInstanceState,
                new Attachments()
                        .attach(SHOW_SERVICE, containerView),
                new Stack()
                        .put(SHOW_SERVICE, new HomeScreen("Initial")));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        architect.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        architect.delegate().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        architect.delegate().onStart();
    }

    @Override
    protected void onStop() {
        architect.delegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        architect.delegate().onDestroy();
        architect = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (architect.delegate().onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }
}