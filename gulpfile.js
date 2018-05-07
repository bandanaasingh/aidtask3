var gulp = require('gulp');
var uglify = require('gulp-uglify');
var rename = require("gulp-rename");
var autoprefixer = require('gulp-autoprefixer');
var debug = require('gulp-debug');
var pump = require('pump');

gulp.task('default', ['compressjs', 'prefixCSS']);

gulp.task('compressjs', function (cb) {
    pump([
            gulp.src(['src/main/webapp/resources/custom/js/*.js', '!src/main/webapp/resources/custom/js/*.min.js']),
            debug({title: 'File:'}),
            uglify(),
            rename({suffix: '.min'}),
            gulp.dest('src/main/webapp/resources/custom/js/')
        ],
        cb
    );
});

gulp.task('prefixCSS', function (cb) {
    pump([
            gulp.src('src/main/webapp/resources/custom/css/styles.min.css'),
            autoprefixer({
                browsers: ['last 20 versions'],
                cascade: false
            }),
            debug({title: 'File:'}),
            gulp.dest('src/main/webapp/resources/custom/css/')
        ],
        cb
    );
});