from flask import Flask, request, jsonify
from flask_jwt_extended import JWTManager, create_access_token, jwt_required, get_jwt_identity
from flask_sqlalchemy import SQLAlchemy
import bcrypt
from flask_cors import CORS
import os
from flask_wtf.csrf import CSRFProtect


app = Flask(__name__)

csrf = CSRFProtect()
csrf.init_app(app)

app.config['JWT_SECRET_KEY'] = os.getenv('JWT_SECRET_KEY')
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
jwt = JWTManager(app)
db = SQLAlchemy(app)
CORS(app)


# Database Model
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)  # Increased length for hashed passwords

# Initialize the database
with app.app_context():
    db.create_all()

@app.route('/register', methods=['POST'])
@csrf.exempt
def register():
    data = request.get_json()
    existing_user = User.query.filter_by(username=data['username']).first()

    if existing_user:
        return jsonify(message="User already exists"), 400

    hashed_password = bcrypt.hashpw(data['password'].encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
    new_user = User(username=data['username'], password=hashed_password)
    db.session.add(new_user)
    db.session.commit()
    return jsonify(message="User registered"), 201

@app.route('/login', methods=['POST'])
@csrf.exempt
def login():
    data = request.get_json()
    user = User.query.filter_by(username=data['username']).first()
    
    if user and bcrypt.checkpw(data['password'].encode('utf-8'), user.password.encode('utf-8')):
        token = create_access_token(identity={'username': user.username})
        return jsonify(token=token)
    return jsonify(message="Invalid credentials"), 401

@app.route('/profile', methods=['GET'])
@csrf.exempt
@jwt_required()
def profile():
    current_user = get_jwt_identity()
    return jsonify(logged_in_as=current_user), 200


if __name__ == '__main__':
    app.run(port=3001)