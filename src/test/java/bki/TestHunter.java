package bki;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import robocode.Rules;
import robocode.control.events.BattleFinishedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IBulletSnapshot;
import robocode.control.snapshot.IDebugProperty;
import robocode.control.snapshot.IRobotSnapshot;
import robocode.control.snapshot.ITurnSnapshot;
import robocode.control.testing.RobotTestBed;

/**
 * Tests the {@link Hunter} robot.
 * 
 * @author Bret K. Ikehara
 */
public class TestHunter extends RobotTestBed {

  private static final String myRobotName = "bki.Hunter*";

  private double positionX = -1, positionY = -1;
  private int positionCheckCount = 0;

  private boolean isBulletFired = false;

  /**
   * Specifies the robots that will fight.
   * 
   * @return The comma-delimited list of robots in this match.
   */
  @Override
  public String getRobotNames() {
    return "sample.Tracker,bki.Hunter";
  }

  /**
   * This test runs for 20 rounds.
   * 
   * @return The number of rounds.
   */
  @Override
  public int getNumRounds() {
    return 10;
  }

  /**
   * Tests that a bullet is fire every turn.
   * 
   * @param event {@link TurnEndedEvent}
   */
  @Override
  public void onTurnEnded(TurnEndedEvent event) {

    checkBullet(event);
    checkPosition(event);

    checkEnemyDeath(event);
  }

  /**
   * Checks Hunter's position.
   * 
   * @param event {@link TurnEndedEvent}
   */
  private void checkPosition(TurnEndedEvent event) {
    ITurnSnapshot turn = event.getTurnSnapshot();

    // test to ensure we aren't in the same place.
    for (IRobotSnapshot robot : turn.getRobots()) {
      if (myRobotName.equals(robot.getName())) {
        if (this.positionX == -1) {
          // default position.
          this.positionX = robot.getX();
          this.positionY = robot.getY();
        }
        else {
          // check previous position with current position.
          boolean checkX = testDouble(this.positionX, robot.getX(), 5.0);
          boolean checkY = testDouble(this.positionY, robot.getY(), 5.0);
          if (checkX && checkY) {
            positionCheckCount += 1;
          }
          else {
            positionCheckCount = 0;
          }

          // same position for more than 10 turns.
          if (positionCheckCount > 10) {
            fail("Robot got stuck on the wall.");
          }
        }
      }
    }
  }

  /**
   * Check whether enemy is killed.
   * 
   * @param event {@link TurnEndedEvent}
   */
  private void checkEnemyDeath(TurnEndedEvent event) {
    ITurnSnapshot turn = event.getTurnSnapshot();
    IRobotSnapshot[] robots = turn.getRobots();
    IRobotSnapshot myRobot = null;
    String propName = "targetName";

    for (IRobotSnapshot robot : robots) {
      if (myRobotName.equals(robot.getName())) {
        myRobot = robot;
      }
    }

    // TODO fix the tests. Will fail sometimes.
    for (IRobotSnapshot robot : robots) {
      if (robot.getEnergy() == 0) {
        for (IDebugProperty prop : myRobot.getDebugProperties()) {
          if (propName.equals(prop.getKey())) {
            assertNull("Hunter should not locked on to any enemy.", prop.getValue());
            break;
          }
        }
      }
    }
  }

  /**
   * Checks the bullet firepower.
   * 
   * @param event {@link TurnEndedEvent}
   */
  private void checkBullet(TurnEndedEvent event) {
    ITurnSnapshot turn = event.getTurnSnapshot();
    IRobotSnapshot[] robots = turn.getRobots();

    // tests the firepower.
    for (IBulletSnapshot bullet : turn.getBullets()) {
      IRobotSnapshot robot = robots[bullet.getOwnerIndex()];

      // check fire power
      if (myRobotName.equals(robot.getName())) {
        this.isBulletFired = true;

        // fire power is limited when life is low.
        if (robot.getEnergy() > 10.0) {
          boolean test = testDouble(Rules.MAX_BULLET_POWER, bullet.getPower(), 0.3);
          assertTrue("Always max fire power: " + bullet.getPower(), test);
        }
        else {
          boolean test = testDouble(Rules.MAX_BULLET_POWER, bullet.getPower(), 3.0);
          assertTrue("Fire as much as possible: " + bullet.getPower(), test);
        }
      }
    }
  }

  /**
   * Checks whether a bullet has been fired since that implies that an enemy robot has been found.
   * 
   * @param event {@link BattleFinishedEvent}
   */
  @Override
  public void onBattleFinished(BattleFinishedEvent event) {
    // true when enemy is found.
    assertTrue("Shot at enemy", this.isBulletFired);
  }

  /**
   * Checks whether a double is equals with offset.
   * 
   * @param expected double
   * @param value double
   * @param epsilon double
   * @return boolean
   */
  public boolean testDouble(double expected, double value, double epsilon) {
    return (expected - epsilon < value) && (expected + epsilon > value);
  }
}