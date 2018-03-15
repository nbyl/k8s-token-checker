# k8s-token-checker

This project will check the validity of the certificates in your service account token.

(If you don't know what I'm talking about, please head over to https://kubernetes.io).

## Installation

### Kubernetes

TBD.

### OpenShift

You can create an S2I build using the following command line:

        oc new-app codecentric/springboot-maven3-centos~https://github.com/nbyl/k8s-token-checker.git
