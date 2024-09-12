# Use the latest stable version of Python with Alpine base
FROM python:3.12-alpine

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Working directory
WORKDIR /user-service

# Install system dependencies
RUN apk update && apk add --no-cache \
    gcc \
    musl-dev \
    libffi-dev \
    postgresql-dev \
    build-base

# Copy the current directory contents into the container
COPY . /user-service

# Upgrade pip and setuptools to latest secure versions
RUN pip install --upgrade pip setuptools==70.0.0

# Install the latest secure dependencies
RUN pip install --no-cache-dir Flask==2.3.3 SQLAlchemy==2.0.21

# Expose the app port
EXPOSE 3001

# Command to run the Flask app
CMD ["flask", "run", "--host=0.0.0.0", "--port=3001"]
