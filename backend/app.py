from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)

if __name__ == '__main__':
    app.run(debug=True) ### REMOVE BEFORE DEPLOY

app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL')
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

db.create_all()

@app.route('/items/<id>', methods=['GET'])
def get_item(id):
    item = Item.query.get(id)
    del item.__dict__['_sa_instance_state']
    return jsonify(item.__dict__)

@app.route('/items', methods=['GET'])
def get_items():
    items = []
    for item in db.session.query(Item).all():
        del item.__dict__['_sa_instance_state']
        items.append(item.__dict__)
    return jsonify(items)

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