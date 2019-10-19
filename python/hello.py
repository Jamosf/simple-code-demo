from flask import Flask, url_for, render_template
app = Flask(__name__)


@app.route('/')
def index():
    return 'index page'


@app.route('/login')
def login(): pass


@app.route('/hello')
def hello_world():
    return 'hello world'


@app.route('/user/<username>')
def show_user_profile(username):
    return 'user %s' % username


@app.route('/post/<int:post_id>')
def show_post(post_id):
    return 'post %d' % post_id


# with app.test_request_context():
#     print url_for('index')
#     print url_for('login')
#     print url_for('login', next='/')


@app.route('/map')
def map():
    return render_template('index.html')


if __name__ == '__main__':
    app.run()
