from flask import Blueprint, request, jsonify, make_response
from flask_jwt_extended import create_access_token, jwt_required, get_jwt_identity, set_access_cookies, unset_jwt_cookies
from services.service import find_user_by_username, create_user 
import bcrypt

user_service_blueprint = Blueprint('user_service', __name__)

# Routes using the service layer
@user_service_blueprint.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    existing_user = find_user_by_username(data['username'])

    if existing_user:
        return jsonify(message="User already exists"), 400

    create_user(data['username'], data['password'])
    return jsonify(message="User registered"), 201

@user_service_blueprint.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    user = find_user_by_username(data['username'])

    if user and bcrypt.checkpw(data['password'].encode('utf-8'), user.password.encode('utf-8')):
        access_token = create_access_token(identity={'username': user.username})

        # Set JWT in a cookie
        response = make_response(jsonify(message="Login successful"))
        set_access_cookies(response, access_token)
        return response
    return jsonify(message="Invalid credentials"), 401

@user_service_blueprint.route('/profile', methods=['GET'])
@jwt_required()
def profile():
    current_user = get_jwt_identity()
    return jsonify(logged_in_as=current_user), 200
