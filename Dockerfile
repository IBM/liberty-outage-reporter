# For reference only

FROM websphere-liberty
ADD target/LibertyOutageReporter.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/
ENV LICENSE accept
EXPOSE 9080

## Running the container locally
# mvn clean install
# docker build -t libertyoutagereporter:latest .
# docker run -d --name myjavacontainer libertyoutagereporter
# docker run -p 9080:9080 --name myjavacontainer libertyoutagereporter
# Visit http://localhost:9080/LibertyOutageReporter/

## Push container to Bluemix
# Install cli and dependencies: https://console.ng.bluemix.net/docs/containers/container_cli_cfic_install.html#container_cli_cfic_install
# docker tag libertyoutagereporter:latest registry.ng.bluemix.net/<my_namespace>/libertyoutagereporter:latest
# docker push registry.ng.bluemix.net/<my_namespace>/libertyoutagereporter:latest
# bx ic images # Verify new image
