# Kubernetes cluster

Our group was provided with three VMs: student87 (master node-145.100.134.87), student85 (worker node- 145.100.134.85) and student86 (worker node- 145.100.134.86). To check information about pods you need to connect remote VM provided by the course for student87. Here are some commands for it:

## Cluster setup
To setup the cluster we can follow the document. We used Kubenetes version 1.23.00 because 1.24.00 doesn't recognize the docker engine on its own.
To install the calico plugin we had to change the Installation resource.

![image](https://user-images.githubusercontent.com/90235167/167476993-ceec52be-3ff1-40ec-a27c-596cfd506630.png)



#### Check the connectivity of the nodes (See if they are ready)
- kubectl get nodes -A -o wide 
#### The pods should be running
- kubectl get pods -A -o wide
#### List of deployments for all the namespaces 
- kubectl get deployments -A -o wide 
#### List of all the services for all the namespaces
- kubectl get services -A -o wide 


## Lets start with the resource installations

### The first thing we install is the metal lb load balancer
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.12.1/manifests/namespace.yaml
kubectl apply -f https://raw.githubusercontent.com/metallb/metallb/v0.12.1/manifests/metallb.yaml

### Let's install the ingress controller(We have used Nginx ingress controller in our assignment)
#### To install the controller we used Kubernetes package manager Helm. It is similar to snap
- git clone https://github.com/nginxinc/kubernetes-ingress.git --branch v2.2.0
- cd kubernetes-ingress/deployments/helm-chart
- helm repo add nginx-stable https://helm.nginx.com/stable
- helm repo update
- helm install my-release nginx-stable/nginx-ingress
- helm install my-release .

## Navigate to kube-yaml directory

### Now we install the mysql 
- Install all the Yaml files.
### Dicovery service
- Install Service and then deployment.
### Install auth service
- Install the secret and configmaps.
- Install the auth service and then the deployment.
### Install the UrlShortener service
- Install the secret and configmaps.
- Install the urlshortenerdeployments and service.
### Install the ingress service
- Inside the Ingress.


If you want to make API calls, you should refer to endpoint 145.100.134.87 at port 30050. For example, use http://145.100.134.87:30050/api/auth/user/ for new user registration. 

## Content

### Auth Service
- Deployment
- Service

### Discovery Service
- Deployment
- Ingress
- Service

### Database - MySql
- Deployment
- Volume
- Service

### UrlShortener Service
- Deployment
- Configmap
- Service

## Sample requests

![image](https://user-images.githubusercontent.com/90235167/167480072-8636676f-f226-45e4-95f6-b43a63792348.png)

![image](https://user-images.githubusercontent.com/90235167/167480409-8e8bed2a-2d41-4353-8026-77c4a695e291.png)

![image](https://user-images.githubusercontent.com/90235167/167480496-3cff7a1e-13cc-4bf8-9b87-13b458c6233b.png)



