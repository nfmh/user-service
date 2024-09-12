# Use an older version of Python to introduce vulnerabilities
FROM python:3.6-slim

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Working directory
WORKDIR /user-service

# Install system dependencies (introducing potential vulnerabilities)
RUN apt-get update && apt-get install -y --no-install-recommends gcc libpq-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the current directory contents into the container
COPY . /user-service

# Install vulnerable dependencies (older versions)
RUN pip install --no-cache-dir Flask==1.0.2 SQLAlchemy==1.3.0

# Expose the app port
EXPOSE 3001

# Command to run the Flask app
CMD ["flask", "run", "--host=0.0.0.0", "--port=3001"]
