package be.vinci.pae.utils.job;

import be.vinci.pae.business.dto.ObjetDTO;
import be.vinci.pae.business.ucc.ObjetUCC;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
   *
   * @param jobExecutionContext the job execution context
   * @throws JobExecutionException if there is an error executing the job
   */
  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    System.out.println("dzedze");
    List<ObjetDTO> objetDTOList = objetUCC.getAllObject().stream()
        .filter(objetDTO -> objetDTO.getEtat().equals("en vente"))
        .filter(objetDTO -> {
          long daysBetween = ChronoUnit.DAYS.between(objetDTO.getDate_depot(), LocalDate.now());
          return daysBetween >= 30;
        })
        .toList();
    objetUCC.retirerObjetVente(objetDTOList);
  }
}
