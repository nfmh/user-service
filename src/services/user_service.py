from app import db
from app.models import User
import bcrypt

def find_user_by_username(username):
    return User.query.filter_by(username=username).first()

def create_user(username, password):
    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')
    new_user = User(username=username, password=hashed_password)
    db.session.add(new_user)
    db.session.commit()
    return new_user
