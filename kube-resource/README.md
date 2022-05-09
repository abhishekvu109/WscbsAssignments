# Kubernetes cluster

Our group was provided with three VMs: student87 (master node), student85 (worker node) and student86 (worker node). To check information about pods you need to connect remote VM provided by the course for student87. Here are some commands for it:

- kubectl get nodes -A -o wide 
- kubectl get pods -A -o wide 
- kubectl get deployments -A -o wide 
- kubectl get services -A -o wide 

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