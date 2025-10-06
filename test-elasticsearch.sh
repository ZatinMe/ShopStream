#!/bin/bash

echo "Testing Elasticsearch connectivity..."

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Test Elasticsearch health
echo "Testing Elasticsearch health endpoint..."
curl -X GET "http://localhost:9200/_cluster/health?pretty"

echo ""
echo "Testing Spring Boot search health endpoint..."
curl -X GET "http://localhost:8080/api/search/health"

echo ""
echo "Testing Kibana..."
curl -X GET "http://localhost:5601/api/status"

echo ""
echo "Elasticsearch and Kibana setup complete!"
echo "Access Kibana at: http://localhost:5601"
echo "Access Elasticsearch at: http://localhost:9200"
