package liccam

case class Car(id: String) {
  var speed : Int = 0
  def getSpeed(): Int = speed
  def setSpeed(s : Int) : Unit = { this.speed = s }
}
