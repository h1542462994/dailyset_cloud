FROM openjdk:11

RUN chsh -s /bin/bash
ENV SHELL=/bin/bash

RUN adduser --gecos '' --disabled-password dailyset

RUN apt-get update -y && \
    apt-get install -y python3-pip python3-dev
ADD bin /home/dailyset
RUN pip install /home/dailyset/new-school-sdk-repaired
RUN ln -s /usr/local/python3/bin/python3.* /usr/bin/python3

EXPOSE 8087
USER dailyset
WORKDIR /home/dailyset
ENTRYPOINT [ "java", "-jar", "/home/dailyset/dailyset_unic-1.0.0.jar"]

