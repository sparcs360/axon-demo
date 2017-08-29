# docker build --tag sparcs/rabbitmq:latest .
FROM rabbitmq

MAINTAINER Lee Newfeld

ENV RABBITMQ_PLUGINS "rabbitmq_management rabbitmq_tracing rabbitmq_management_visualiser rabbitmq_shovel rabbitmq_shovel_management rabbitmq_stomp"
ENV RABBITMQ_HOME /var/lib/rabbitmq

RUN rabbitmq-plugins enable --offline $RABBITMQ_PLUGINS

# Management Web UI (over http://)
EXPOSE 15672
# STOMP socket
EXPOSE 61613
