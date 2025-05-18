resource "rabbitmq_vhost" "dev" {
  name = "dev"
}

resource "rabbitmq_user" "admin" {
  name     = "admin"
  password = "admin"
  tags = ["administrator"]
}

resource "rabbitmq_permissions" "admin" {
  user  = rabbitmq_user.admin.name
  vhost = rabbitmq_vhost.dev.name

  permissions {
    configure = ".*"
    write     = ".*"
    read      = ".*"
  }
}

resource "rabbitmq_exchange" "create_financial_record" {
  name  = "create_financial_record"
  vhost = rabbitmq_permissions.admin.vhost

  settings {
    type        = "fanout"
    durable     = false
    auto_delete = true
  }
}

resource "rabbitmq_exchange" "create_financial_record_dlq" {
  name  = "create_financial_record_dlq"
  vhost = rabbitmq_permissions.admin.vhost

  settings {
    type        = "direct"
    durable     = false
    auto_delete = true
  }
}

resource "rabbitmq_queue" "create_financial_record" {
  name  = "create_financial_record"
  vhost = rabbitmq_permissions.admin.vhost

  settings {
    durable     = false
    auto_delete = true

    arguments = {
      "x-dead-letter-exchange"    = rabbitmq_exchange.create_financial_record_dlq.name
      "x-dead-letter-routing-key" = "dead_letter"
    }
  }
}

resource "rabbitmq_queue" "create_financial_record_dlq" {
  name  = "create_financial_record_dlq"
  vhost = rabbitmq_permissions.admin.vhost

  settings {
    durable     = false
    auto_delete = true
  }
}

resource "rabbitmq_binding" "create_financial_record" {
  source           = rabbitmq_exchange.create_financial_record.name
  destination      = rabbitmq_queue.create_financial_record.name
  destination_type = "queue"
  vhost            = rabbitmq_permissions.admin.vhost
  routing_key      = "#"
}

resource "rabbitmq_binding" "create_financial_record_dlq" {
  source           = rabbitmq_exchange.create_financial_record_dlq.name
  destination      = rabbitmq_queue.create_financial_record_dlq.name
  destination_type = "queue"
  vhost            = rabbitmq_permissions.admin.vhost
  routing_key      = "dead_letter"
}