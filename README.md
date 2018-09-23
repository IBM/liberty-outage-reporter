# liberty-outage-reporter

# Getting started with Liberty on Bluemix
By following this guide, you'll set up a development environment, deploy an app locally and on Bluemix, and integrate a Bluemix database service in your app.

## Prerequisites

[Git](https://git-scm.com/downloads), [Bluemix CLI](https://clis.ng.bluemix.net/ui/home.html), [Maven](https://maven.apache.org/download.cgi), and a [Bluemix account](https://console.ng.bluemix.net/registration/).

## 1. Clone the sample app

Now you're ready to start working with the app. Clone the repo and change the directory to where the sample app is located.
  ```bash
  git clone https://github.com/IBM/liberty-outage-reporter
  cd liberty-outage-reporter
  ```

## 2. Run the app locally using command line

Use Maven to install dependencies and build the .war file.

  ```
  mvn clean install
  ```

Run the app locally on Liberty.
  ```
  mvn install liberty:run-server
  ```

View your app at: http://localhost:9080/LibertyOutageReporter


## 3. Deploy to Bluemix using command line

To deploy to Bluemix using command line, it can be helpful to set up a manifest.yml file. The manifest.yml includes basic information about your app, such as the name, the location of your app, how much memory to allocate for each instance, and how many instances to create on startup. This is also where you'll choose your URL. [Learn more...](https://console.bluemix.net/docs/manageapps/depapps.html#appmanifest)

The manifest.yml is provided in the sample.

  ```
  applications:
  - path: target/LibertyOutageReporter.war
    memory: 512M
    instances: 1
    name: your-appname-here
    host: your-appname-here
  ```

Change both the *name* and *host* to a single unique name of your choice. Note that the *host* value will be used in your public url, for example, http://your-appname-here.mybluemix.net. If you already created an app from the Bluemix UI but haven't pushed your code to it, you can use the same name value. Make sure the path points to the built application, for this example the location is `target/LibertyOutageReporter.war`.

Choose your API endpoint
   ```
   bluemix api <API-endpoint>
   ```

Replace the *API-endpoint* in the command with an API endpoint from the following list.
* https://api.ng.bluemix.net # US South
* https://api.eu-de.bluemix.net # Germany
* https://api.eu-gb.bluemix.net # United Kingdom
* https://api.au-syd.bluemix.net # Sydney

Login to your Bluemix account
  ```
  bluemix login
  ```

Push your application to Bluemix.
  ```
  bluemix app push
  ```

This can take around two minutes. If there is an error in the deployment process you can use the command `cf logs <Your-App-Name> --recent` to troubleshoot.

## 4. Add a database

Next, we'll add a NoSQL database to this application and set up the application so that it can run locally and on Bluemix.

1. Log in to Bluemix in your Browser. Select your application and click on `Connect new` under `Connections`.
2. Select `Cloudant NoSQL DB` and Create the service.
3. Select `Restage` when prompted. Bluemix will restart your application and provide the database credentials to your application using the `VCAP_SERVICES` environment variable. This environment variable is only available to the application when it is running on Bluemix.

## 5. Use the database

We're now going to update your local code to point to this database. We'll store the credentials for the services in a properties file. This file will get used ONLY when the application is running locally. When running in Bluemix, the credentials will be read from the `VCAP_SERVICES` environment variable.

1. In Eclipse, open the file `src/main/resources/cloudant.properties`:
  ```
  cloudant_url=
  ```

2. In your browser open the Bluemix UI, select your `App` -> `Connections` -> `Cloudant` -> `View Credentials` (Note you may have create a set of credentials)

3. Copy and paste just the `url` from the credentials to the `url` field of the `cloudant.properties` file.

4. Your Liberty server in Eclipse should automatically pick up the changes and restart the application.

  View your app at: http://localhost:9080/LibertyOutageReporter/. Any names you enter into the app will now get added to the database.

  Make any changes you want and re-deploy to Bluemix!

## 6. Developing and Deploying using Eclipse

  IBM® Eclipse Tools for Bluemix provides plug-ins that can be installed into an existing Eclipse environment to assist in integrating the developer's integrated development environment (IDE) with Bluemix.

  1. Download and install  [IBM Eclipse Tools for Bluemix](https://developer.ibm.com/wasdev/downloads/#asset/tools-IBM_Eclipse_Tools_for_Bluemix).

  2. Import this sample into Eclipse using `File` -> `Import` -> `Maven` -> `Existing Maven Projects` option.

  3. Create a Liberty server definition:
    - In the `Servers` view right-click -> `New` -> `Server`
    - Select `IBM` -> `WebSphere Application Server Liberty`
    - Choose `Install from an archive or a repository`
    - Enter a destination path (`/Users/username/liberty`)
    - Choose `WAS Liberty with Java EE 7 Web Profile`
    - Continue the wizard with default options to Finish

  4. Run your application locally on Liberty:
    - Right click on the `LibertyOutageReporter` sample and select `Run As` -> `Run on Server` option
    - Find and select the localhost Liberty server and press `Finish`
    - In a few seconds, your application should be running at http://localhost:9080/LibertyOutageReporter/

  5. Create a Bluemix server definition:
    - In the `Servers` view, right-click -> `New` -> `Server`
    - Select `IBM` -> `IBM Bluemix` and follow the steps in the wizard.\
    - Enter your credentials and click `Next`
    - Select your `org` and `space` and click `Finish`

  6. Run your application on Bluemix:
    - Right click on the `LibertyOutageReporter` sample and select `Run As` -> `Run on Server` option
    - Find and select the `IBM Bluemix` and press `Finish`
    - A wizard will guide you with the deployment options. Be sure to choose a unique `Name` for your application
    - In a few minutes, your application should be running at the URL you chose.

  Now you have your code running locally and on the cloud!

  The `IBM Eclipse Tools for Bluemix` provides many powerful features such as incremental updates, remote debugging, pushing packaged servers, etc. [Learn more](https://console.ng.bluemix.net/docs/manageapps/eclipsetools/eclipsetools.html#eclipsetools)

# Deploying to Kubernetes
One deployment option is to deploy this application to kubernetes. For this option, we will provision a lite kubernetes cluster via the IBM Bluemix Container Service and deploy the service using the [manifests/deployment.yml](/manifests/deployment.yml) file. We will be using the IBM Container Registry to store the Docker image for our application.

#### Install Bluemix CLI Plugins
1. Install the IBM Bluemix Container Service plug-in.
```sh
bx plugin install container-service -r Bluemix
bx plugin list # To verify the plugin has been installed
bx cs init # Initialize the container service plugin
```
2. Install the IBM Container Registry plug-in and login
```sh
bx plugin install container-registry -r Bluemix
bx cr login
```
3. Create a namespace in the Container Registry service. Note this namespace needs to be unique across all users of Bluemix.
```sh 
bx cr namespace-add [your namespace]
bx cr namespace-list
```

#### Setup IBM Bluemix Container Service
1) Log into the [Bluemix console](https://console.ng.bluemix.net/) and create a lite [Kubernetes cluster](https://console.bluemix.net/containers-kubernetes/launch) named `mycluster`. This will take several minutes to provision. 
2. Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/). Kubectl is the standard way to interact with Kubernetes clusters. You will configure `kubectl` to point to your Kubernetes cluster hosted on the IBM Bluemix Container Service.
3. When the Bluemix Console shows that your cluster is ready, list the clusters from the command line. You should see the cluster you created: `mycluster`. If you don't, make sure you have waited enough time for the cluster to be provisioned. 
```sh
bx cs clusters
```
4. Configure your `kubectl` to point to your Kubernetes cluster.
First print the configuration for your cluster.
```sh
bx cs cluster-config mycluster
```
This should give you an `export KUBE_CONFIG` line. Copy and paste this to configure your `kubectl` to point to your Kubernetes cluster.
For example:
```
export KUBECONFIG=/Users/johnzaccone/.bluemix/plugins/container-service/clusters/mycluster/kube-config-par01-mycluster.yml
```
5. Verify that your `kubectl` is configured. The following should show: `mycluster`.
```sh
kubectl config get-clusters
```
You can now send commands to your Kubernetes cluster!

#### Bind the Cloudant Service
We will use the Bluemix CLI to create a binding between our Cloudant instance and our Kubernetes cluster.
1. List the Cloudant service that you created eariler in this lab. If you haven't created one, refer back to the [Add a Database](#add-a-database) section.
```sh
bx service list | grep cloudant
```
2. Use the name of your Cloudant service in the command below to bind the service.
```sh
bx cs cluster-service-bind mycluster default [Cloudant service Name]
```
The command above loads a Kubernetes secret into your cluster that contains information to connect to your Cloudant instance. By managing your Cloudant credentials via secrets, you avoid saving them in plain text inside your source code repository.

3. List the secrets of the Kubernetes cluster. There might be a few, but you should see the one that was just created for your Cloudant database. We are going to reference the name of this secret in our deployment in the next step.

```sh
kubectl get secrets
```

#### Configure Your Application to Read Kubernetes Secrets
You need to configure your application to read the Kubernetes secret you just created. This will require two changes. 
1. Open the `src/main/resources/cloudant.properties` file. And uncomment the line that specifies the Kubernetes secret file. Also, comment out the line that explicitly sets the Cloudant URL. Your resulting properties file should look something like this:
```
# These properties are meant for local development only and will not be read when application is running in Bluemix.
# When  running in Bluemix, the credentials of bound services are available in the VCAP_SERVICES environment variable.
# cloudant_url=

# You can bind Bluemix services using the `bx cs cluster-service-bind` command. 
# Using this command to bind your cloudant database will load connection information into your kubernetes cluster as a kubernetes secret. 
# Uncomment the following line to tell this app to load the cloudant username/password from the kubernetes secret.

kubernetes_secrets_file=/etc/cloudant-secrets/binding
```
The location for the secret is: `/etc/cloudant-secrets/binding`. This is configured for you already in the `manifests/deployment.yml` file. There is no need to change this path at this time.

2. Open the `manifests/deployment.yml` file. Replace `my-secret` in the container spec with the name of your secret. You can get a list of secrets loaded into your cluster with `kubectl get secrets`. Note that the `etc/cloudant-secrets/binding` path has already been configured for you. Close and save the file.

#### Build and Push your Docker image
1. Build your application
```sh
mvn clean install
```
2. Build and push the Docker image you the IBM Container Registry. Use the namespace you created earlier in the following command. You can list the namespace you created using `bx cr namespace-list`.
```sh
export CR_NAMESPACE=[your namespace]
docker build -t registry.ng.bluemix.net/$CR_NAMESPACE/liberty-outage-reporter .
docker push registry.ng.bluemix.net/$CR_NAMESPACE/liberty-outage-reporter
```
3. Edit the `manifests/deployment.yml` to reference the name of the image that you just pushed. Search for the line

`image: registry.ng.bluemix.net/mynamespace/liberty-outage-reporter`

And replace `mynamespace` with your namespace. For example, if you used namespace `dev` the line would be: 

`image: registry.ng.bluemix.net/dev/liberty-outage-reporter`

#### Deploy the Application
1. Use `kubectl to deploy the application:
```sh
kubectl apply -f manifests
```

It should take a few minutes for the application to deploy. You can check the status by using `kubectl get deploy`.

2. Check that your application has been started by printing the logs.
```sh
kubectl get pods
NAME                                      READY     STATUS    RESTARTS   AGE
liberty-outage-reporter-495303238-65dfm   1/1       Running   0          2m
```
```
kubectl logs -f liberty-outage-reporter-495303238-65dfm
```

#### Test the Application
The deployment uses NodePort to expose the application on all nodes in the cluster. To access the application, first get the IP of the worker node in the cluster.
```sh
kubectl get nodes
NAME              STATUS    AGE       VERSION
184.172.247.191   Ready     8d        v1.5.6-4+abe34653415733
```
Then lists the services to get the port for your application.
```sh
kubectl get services
NAME                  CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
liberty-outage-reporter-service   10.10.10.177   <nodes>       9080:30052/TCP   20min
```

In this example, the application is running at: `http://184.172.247.191:30052/LibertyOutageReporter`. 
