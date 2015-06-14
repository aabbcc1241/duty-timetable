package dutytimetable.model

/**
 * Created by beenotung on 6/14/15.
 */
object GlobalStatus extends ProgressiveModelTrait {
  task_=(DefaultTask.clone().asInstanceOf[TransparentTask[Void]])
  override val Stage_Final: Double = 0
}
