/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import java.util.*;
import org.jgap.*;

/**
 * Monitors the evolution and stops it if evolution does not make a progress
 * as desired.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public class FitnessImprovementMonitor
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int m_initialWaitSeconds;

  private int m_checkIntervalSeconds;

  private double m_improvedFitnessExpected;

  private long m_startMillis;

  private long m_lastCheckMillis;

  private double m_bestFitnessPreviously;

  private int m_checks;

  /**
   * Constructor.
   *
   * @param a_initialWaitSeconds number of seconds to wait until first check
   * @param a_checkIntervalSeconds number of seconds to wait after the previous
   * check (except for the first check, where a_initialWaitSeconds is taken)
   * @param a_improvedFitnessExpected number of fitness units the current best
   * solution evolved is better than the best solution from the previously check
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public FitnessImprovementMonitor(int a_initialWaitSeconds,
                                   int a_checkIntervalSeconds,
                                   double a_improvedFitnessExpected) {
    m_initialWaitSeconds = a_initialWaitSeconds;
    m_checkIntervalSeconds = a_checkIntervalSeconds;
    m_improvedFitnessExpected = a_improvedFitnessExpected;
    m_bestFitnessPreviously = FitnessFunction.NO_FITNESS_VALUE;
  }

  /**
   * Called after another evolution cycle has been executed.
   *
   * @param a_pop the currently evolved population
   * @param a_messages the monitor can append messages here to indicate why
   * it asks evolution to stop
   * @return true: continue with the evolution; false: stop evolution
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public boolean nextCycle(Population a_pop, List<String> a_messages) {
    long currentMillis = System.currentTimeMillis();
    boolean doCheck = false;
    if (m_checks == 0) {
      if (currentMillis - m_startMillis >= m_initialWaitSeconds * 1000) {
        doCheck = true;
      }
    }
    else {
      if (currentMillis - m_lastCheckMillis >= m_checkIntervalSeconds * 1000) {
        doCheck = true;
      }
    }
    if (doCheck) {
      // Let's verify the progress since our last check.
      // -----------------------------------------------
      IChromosome best = a_pop.determineFittestChromosome();
      if (best != null) {
        // A best solution exists.
        // -----------------------
        if (Math.abs(m_bestFitnessPreviously - FitnessFunction.NO_FITNESS_VALUE) <
            FitnessFunction.DELTA) {
          // There was no previous best solution.
          // ------------------------------------
          m_bestFitnessPreviously = best.getFitnessValue();
        }
        else {
          // Is the current best solution better than the previous one?
          // ----------------------------------------------------------
          if (Math.abs(best.getFitnessValue() - m_bestFitnessPreviously) <
              m_improvedFitnessExpected) {
            // Bad luck, not enough progress.
            // ------------------------------
            a_messages.add("Not enough progress was made");
            return false;
          }
          else {
            m_bestFitnessPreviously = best.getFitnessValue();
          }
        }
      }
      else {
        if (m_checks > 0) {
          // No result evolved during two check cycles.
          // ------------------------------------------
          a_messages.add(
              "No solution at all was evolved during two check cycles.");
          return false;
        }
      }
      m_lastCheckMillis = System.currentTimeMillis();
      m_checks++;
    }
    // No check needed yet.
    // --------------------
    return true;
  }

  /**
   * Called just before the evolution starts.
   *
   * @param a_config the configuration used
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public void start(Configuration a_config) {
    m_startMillis = System.currentTimeMillis();
  }
}
