# Use the latest stable version of Python with Alpine base
FROM python:3.12-alpine

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1
ENV FLASK_APP=app:create_app
ENV PYTHONPATH=/user-service/src

# Working directory
WORKDIR /user-service

# Install system dependencies
RUN apk update && apk add --no-cache \
    gcc \
    musl-dev \
    libffi-dev \
    postgresql-dev \
    build-base

# Install Gunicorn before switching users
RUN pip install --upgrade pip setuptools==70.0.0
RUN pip install gunicorn

# Create a non-root user and group with a fixed UID and GID
RUN addgroup -g 1001 -S appgroup && adduser -u 1001 -S appuser -G appgroup

# Set permissions on the working directory
RUN chown -R appuser:appgroup /user-service

# Switch to the non-root user
USER appuser

# Copy the current directory contents into the container
COPY . /user-service

# Install the dependencies from requirements.txt
COPY requirements.txt /user-service/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt

# Expose the app port
EXPOSE 3001

# Use Gunicorn as the WSGI server
CMD ["gunicorn", "--bind", "0.0.0.0:3001", "app:create_app"]