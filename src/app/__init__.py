from flask import Flask, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager
from flask_wtf.csrf import CSRFProtect, generate_csrf
from flask_cors import CORS
import os
from flask import request, make_response

# Initialize extensions
db = SQLAlchemy()
jwt = JWTManager()
csrf = CSRFProtect()

def create_app():
    app = Flask(__name__)

    # Load environment variables early
    from dotenv import load_dotenv
    load_dotenv()

    # Application configurations
    app.config['JWT_SECRET_KEY'] = os.getenv('JWT_SECRET_KEY')  
    app.config['JWT_TOKEN_LOCATION'] = ['cookies']
    app.config['JWT_COOKIE_CSRF_PROTECT'] = False
    app.config['JWT_ACCESS_COOKIE_PATH'] = '/'
    app.config['JWT_COOKIE_SECURE'] = False
    app.config['JWT_COOKIE_SAMESITE'] = 'None'
    app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    # ** CSRF Protection Key **
    app.config['SECRET_KEY'] = os.getenv('CSRF_SECRET_KEY')

    app.config['TESTING'] = os.getenv('FLASK_ENV') == 'testing'
    if not app.config['TESTING']:
        csrf.init_app(app)

    # Print the SQLALCHEMY_DATABASE_URI to ensure it's using the right one
    print(f"Using Database URI: {app.config['SQLALCHEMY_DATABASE_URI']}")

    # Add route to return CSRF token
    @app.route('/csrf-token', methods=['GET'])
    def get_csrf_token():
        token = generate_csrf()
        response = make_response({'csrf_token': token})
        response.set_cookie('csrf_token', token, httponly=False)  
        return response

    # Initialize extensions with the app
    db.init_app(app)
    jwt.init_app(app)

    # CORS setup
    if os.getenv('FLASK_ENV') == 'production':
         CORS(app, resources={r"/*": {"origins": '*'}})
    else:
        allowed_origins = os.getenv('ALLOWED_ORIGINS', '*')
        CORS(app, resources={r"/*": {"origins": '*'}})

    # Register blueprints
    from app.user_service import user_service_blueprint
    app.register_blueprint(user_service_blueprint)

    return app
