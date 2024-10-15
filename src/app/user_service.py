from flask import Blueprint, request, jsonify, make_response
from flask_jwt_extended import jwt_required, create_access_token, create_refresh_token, get_jwt_identity, set_access_cookies, set_refresh_cookies, unset_jwt_cookies
from services.service import find_user_by_username, create_user
import bcrypt
import logging

user_service_blueprint = Blueprint('user_service', __name__)
logger = logging.getLogger(__name__)

# Routes using the service layer

# Register route
@user_service_blueprint.route('/register', methods=['POST'])
def register():
    try:
        data = request.get_json()
        existing_user = find_user_by_username(data['username'])

        if existing_user:
            return jsonify(message="User already exists"), 400

        create_user(data['username'], data['password'])
        return jsonify(message="User registered"), 201
    except Exception as e:
        logger.error(f"Error during registration: {str(e)}")
        return jsonify(message="Internal server error"), 500


# Login route
@user_service_blueprint.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json()
        user = find_user_by_username(data['username'])

        if user and bcrypt.checkpw(data['password'].encode('utf-8'), user.password.encode('utf-8')):
            access_token = create_access_token(identity={'username': user.username})
            refresh_token = create_refresh_token(identity={'username': user.username})

            # Set JWT in a cookie
            response = make_response(jsonify(message="Login successful"))
            set_access_cookies(response, access_token)
            set_refresh_cookies(response, refresh_token)
            return response
        else:
            logger.info(f"Failed login attempt for username: {data['username']}")
            return jsonify(message="Invalid credentials"), 401
    except Exception as e:
        logger.error(f"Error during login: {str(e)}")
        return jsonify(message="Internal server error"), 500


# Profile route (protected)
@user_service_blueprint.route('/profile', methods=['GET'])
@jwt_required()
def profile():
    current_user = get_jwt_identity()
    return jsonify(logged_in_as=current_user), 200


# Refresh token route
@user_service_blueprint.route('/refresh', methods=['POST'])
@jwt_required(refresh=True)
def refresh():
    current_user = get_jwt_identity()
    new_access_token = create_access_token(identity=current_user)

    response = make_response(jsonify(message="Token refreshed"))
    set_access_cookies(response, new_access_token)
    return response


# Logout route
@user_service_blueprint.route('/logout', methods=['POST'])
def logout():
    response = make_response(jsonify(message="Logged out successfully"))
    unset_jwt_cookies(response)
    return response