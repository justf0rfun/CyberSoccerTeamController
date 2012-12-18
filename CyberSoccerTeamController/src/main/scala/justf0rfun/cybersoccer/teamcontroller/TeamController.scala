package justf0rfun.cybersoccer.teamcontroller
import scala.collection.mutable.ArrayBuffer
import akka.actor.Actor
import justf0rfun.cybersoccer.centralnervoussystems.PeripheralNervousSystem
import justf0rfun.cybersoccer.centralnervoussystems.CentralNervousSystemFactory
import justf0rfun.cybersoccer.centralnervoussystems.CentralNervousSystem

abstract class TeamController extends CentralNervousSystemFactory with Actor {

  val players = new ArrayBuffer[PeripheralNervousSystem]()
  var numberOfPlayersWithControl = 0
  
  def think()
  
  def createCentralNervousSystem(peripheralNervousSystem: PeripheralNervousSystem) = {
    players += peripheralNervousSystem// the immutable list is used! is this line really correct?
    new TeamSlave(peripheralNervousSystem)
  }
  
  def receive = {
    case 'forwardControl => {
      numberOfPlayersWithControl += 1
      if(players.size == numberOfPlayersWithControl) {
        think()
        numberOfPlayersWithControl = 0
      }
    }
  }
  
}

class TeamSlave(private val peripheralNervousSystem: PeripheralNervousSystem) extends CentralNervousSystem(peripheralNervousSystem) {
  
  def think(): Unit = 'forwardControl
  
}