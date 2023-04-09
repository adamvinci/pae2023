package be.vinci.pae.utils.scheduler;

import be.vinci.pae.utils.ApplicationBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * A Quartz JobFactory implementation that uses HK2 to manage dependencies for job instances.
 */
public class MyJobFactory implements JobFactory {

  private ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  /**
   * Creates a new job instance using HK2 to manage dependencies.
   *
   * @param triggerFiredBundle the bundle containing the job detail and trigger
   * @param scheduler the scheduler instance
   * @return a new job instance with dependencies injected
   * @throws SchedulerException if there is an error instantiating the job instance
   */
  @Override
  public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler)
      throws SchedulerException {
    Class<? extends Job> jobClass = triggerFiredBundle.getJobDetail().getJobClass();
    return locator.getService(jobClass);
  }
}
