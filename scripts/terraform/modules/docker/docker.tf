resource "docker_image" "rabbitmq" {
  name         = "rabbitmq:4-management"
  keep_locally = true
}

resource "docker_container" "myexpenses_rabbitmq" {
  name  = "myexpenses_rabbitmq"
  image = docker_image.rabbitmq.image_id
  ports {
    internal = 5672
    external = 5672
  }
  ports {
    internal = 15672
    external = 15672
  }
}