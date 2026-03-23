PROJECT_NAME=billing-the-bot
REGISTRY_HOST=rodrigo

.PHONY: help
help:
	@echo 'Usage: make <target>'
	@echo 'Targets:'
	@echo '  docker/build - Build the Docker image for the project'


.PHONY: all
all: help

.PHONY: docker/build
docker/build:
	docker build --tag '$(REGISTRY_HOST)/$(PROJECT_NAME):v1' -f Dockerfile .

.PHONY: docker/up
docker/up: docker/build
	docker compose up