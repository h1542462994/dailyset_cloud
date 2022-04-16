FROM openjdk:11

RUN chsh -s /bin/bash
ENV SHELL=/bin/bash

RUN adduser --gecos '' --disabled-password dailyset

ADD bin /home/dailyset

EXPOSE 8086
USER dailyset
WORKDIR /home/dailyset
ENTRYPOINT [ "java", "-jar", "/home/dailyset/dailyset_cloud-1.0.0.jar"]