# Use the latest stable and secure version of Python
FROM python:3.12-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Working directory
WORKDIR /user-service

# Install system dependencies
RUN apt-get update && apt-get install -y --no-install-recommends gcc libpq-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the current directory contents into the container
COPY . /user-service

# Install the latest setuptools to resolve the vulnerability
RUN pip install --upgrade pip setuptools==70.0.0

# Install latest secure dependencies
RUN pip install --no-cache-dir Flask==2.3.3 SQLAlchemy==2.0.21

# Expose the app port
EXPOSE 3001

# Command to run the Flask app
CMD ["flask", "run", "--host=0.0.0.0", "--port=3001"]
