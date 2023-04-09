package be.vinci.pae.utils.Job;

import be.vinci.pae.business.ucc.ObjetUCC;
import jakarta.inject.Inject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * A job that performs a task related to an object sale.
 */
public class MyJob implements Job {
  @Inject
  private ObjetUCC objetUCC;

  /**
   * Executes the task related to an object sale.
   * @param jobExecutionContext the job execution context
   * @throws JobExecutionException if there is an error executing the job
   */
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    System.out.println("dzedze");
    objetUCC.retirerObjetVente();
  }
}
