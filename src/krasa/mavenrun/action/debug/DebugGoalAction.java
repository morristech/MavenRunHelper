package krasa.mavenrun.action.debug;

import javax.swing.*;

import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.project.Project;
import krasa.mavenrun.action.RunGoalAction;
import krasa.mavenrun.model.Goal;
import krasa.mavenrun.utils.MavenDebugConfigurationType;

import org.jetbrains.idea.maven.execution.MavenRunnerParameters;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

import com.intellij.openapi.actionSystem.DataContext;

public class DebugGoalAction extends RunGoalAction {

	public DebugGoalAction(String goal, Icon icon) {
		super(goal, icon);
	}

	public DebugGoalAction(Goal goal, Icon icon) {
		super(goal, icon);
	}

	@Override
	protected void run(final DataContext context, final MavenRunnerParameters params) {
		params.getGoals().add("-DforkMode=never");
		runInternal(MavenActionUtil.getProject(context), params);
	}

	private void runInternal(final Project project, final MavenRunnerParameters params) {
		MavenDebugConfigurationType.debugConfiguration(project, params, new ProgramRunner.Callback() {
			@Override
			public void processStarted(RunContentDescriptor descriptor) {
				descriptor.setRestarter(new Runnable() {
					@Override
					public void run() {
						DebugGoalAction.this.runInternal(project, params);
					}
				});
			}
		});
	}
}
