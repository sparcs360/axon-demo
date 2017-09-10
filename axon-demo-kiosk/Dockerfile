FROM sparcs/java8:latest

MAINTAINER Lee Newfeld

ADD target/axon-demo-kiosk-*.jar /app.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "-Dinfo.shop.id=${SHOP_ID}", "-Dinfo.kiosk-index=${KIOSK_INDEX}", "/app.jar"]

EXPOSE 8080
