terraform {
  required_version = ">= 1.0.0"

  required_providers {
    rabbitmq = {
      source  = "cyrilgdn/rabbitmq"
      version = "1.8.0"
    }
  }
}

provider "rabbitmq" {
  endpoint = "http://localhost:15672"
  username = "guest"
  password = "guest"
}