from flask import Flask, jsonify, request, abort,  make_response
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from sqlalchemy.exc import NoResultFound
import jwt
from werkzeug.security import generate_password_hash, check_password_hash
import uuid
import os
import sys

app = Flask(__name__)

if __name__ == '__main__':
    app.run(debug=True) ### REMOVE BEFORE DEPLOY

CORS(app)

app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL')
app.config['SECRET_KEY'] = os.environ.get('SECRET_KEY')
db = SQLAlchemy(app)

class Item(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    ean  = db.Column(db.String(20), unique=True, nullable=False)
    name = db.Column(db.String(80), unique=False, nullable=False)
    location = db.Column(db.String(100), unique=False, nullable=False)
    quantity = db.Column(db.Integer, unique=False)
    

    def __init__(self, ean, name, location, quantity):
        self.ean = ean
        self.name = name
        self.location = location
        self.quantity = quantity

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    public_id = db.Column(db.String(40))
    username = db.Column(db.String(100), index=True, unique=True)
    password = db.Column(db.String(100))
    admin = db.Column(db.Boolean)

    def __repr__(self):
      return '<User {}>'.format(self.username)


db.create_all()


@app.route('/login', methods=['POST'])
def login():
    auth = request.get_json()
    if not auth or not auth.get('username') or not auth.get('password'):
        return make_response('Could not verify!', 401, {'WWW-Authenticate': 'Basic-realm= "Login required!"'})

    user = User.query.filter_by(username=auth['username']).first()
    if not user:
        return make_response('Could not verify user, Please signup!', 401, {'WWW-Authenticate': 'Basic-realm= "No user found!"'})

    if check_password_hash(user.password, auth.get('password')):
        token = jwt.encode({'public_id': user.public_id}, app.config['SECRET_KEY'], 'HS256')
        return make_response(jsonify({'token': token}), 201)

    return make_response('Could not verify password!', 403, {'WWW-Authenticate': 'Basic-realm= "Wrong Password!"'})

@app.route('/signup', methods=['POST'])
def signup_user(): 
    data = request.get_json() 
    hashed_password = generate_password_hash(data['password'], method='sha256')
    
    user = User.query.filter_by(username=data['username']).first()
    if not user:
        new_user = User(public_id=str(uuid.uuid4()), username=data['username'], password=hashed_password, admin=False)
        db.session.add(new_user) 
        db.session.commit() 

        return jsonify({'message': 'registered successfully'}), 201
    else:
        return make_response(jsonify({"message": "User already exists!"}), 409)


def check_if_logged_in():
  token = None

  if 'x-access-token' in request.headers:
      token = request.headers['x-access-token']
  if not token: 
      abort(401)
  try:
      data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
      current_user = User.query.filter_by(public_id=data['public_id']).first()
      if not current_user:
        abort(403)
  except:
      abort(401)



@app.route('/items/<it_id>', methods=['GET'])

def get_item(it_id):
    item = Item.query.get(it_id)
    del item.__dict__['_sa_instance_state']
    return jsonify(item.__dict__)

@app.route('/items', methods=['GET'])
def get_items():
    items = []
    for item in db.session.query(Item).all():
        del item.__dict__['_sa_instance_state']
        items.append(item.__dict__)
    return jsonify(items)

@app.route('/items-by-ean/<ean>', methods=['GET'])
def get_items_by_ean(ean):
    print(ean, file=sys.stderr)

    try:
      item = db.session.query(Item).filter(Item.ean == ean).one()
    except NoResultFound:
      abort(404)
    del item.__dict__['_sa_instance_state']
    return jsonify(item.__dict__)


@app.route('/items', methods=['POST'])
def create_item():
  body = request.get_json()
  db.session.add(Item(ean=body['ean'], name=body['name'], location=body['location'], quantity=body['quantity']))
  db.session.commit()
  return "item created"

@app.route('/items/<id>', methods=['PUT'])
def update_item(id):
  body = request.get_json()
  db.session.query(Item).filter_by(id=id).update(
      #ean, name, location, quantity
    dict(ean=body['ean'], name=body['name'], location=body['location'], quantity=body['quantity'])) 
  db.session.commit()
  return "item updated"

@app.route('/items/<id>', methods=['DELETE'])
def delete_item(id):
  db.session.query(Item).filter_by(id=id).delete()
  db.session.commit()
  return "item deleted"