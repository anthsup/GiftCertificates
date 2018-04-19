const { resolve } = require('path');
const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');
const ExtractTextPlugin = require("extract-text-webpack-plugin");

const PATHS = {
    output: resolve(__dirname, 'dist/'),
    app: resolve(__dirname, 'app/app.module.js'),
    nodeModules: resolve(__dirname, 'node_modules')
};

module.exports = () => {

    const webpackConfig = {
        entry: {
            app: PATHS.app
        },
        output: {
            filename: '[name].bundle.js',
            path: PATHS.output,
            publicPath: './'
        },
        resolve: {
            modules: [
                PATHS.nodeModules
            ]
        },
        devtool: 'source-map',

        module: {
            rules: [
                {
                    test: /\.js$/,
                    exclude: /node_modules/,
                    loader: 'babel-loader'
                },
                {
                    test: /\.html$/,
                    loader: 'raw-loader'
                },
                {
                    test: /\.scss$/,
                    use: [{
                        loader: "style-loader"
                    },
                        {
                            loader: "css-loader"
                        },
                        {
                            loader: "sass-loader"
                        }]
                }
            ]
        },

        plugins: [
            new HtmlWebpackPlugin({
                template: 'app/index.html',
                filename: 'index.html',
                inject: 'body'
            }),
            new CleanWebpackPlugin(['dist']),
            new ExtractTextPlugin('css/app.css')
        ]
    };

    return webpackConfig;
};