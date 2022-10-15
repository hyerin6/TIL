module.exports = {
    publicPath: './',
    configureWebpack: {
        devtool: 'source-map'
    },
    pages:{
        index: {
            entry: 'src/main.js',
            template: 'public/index.html',
            filename: 'index.html'
        },
        page1: {
            entry: 'src/pages/page1/main.js',
            template: 'public/index.html',
            filename: 'page1.html'
        },
        page2: {
            entry: 'src/pages/page2/main.js',
            template: 'public/index.html',
            filename: 'page2.html'
        },
        page3: {
            entry: 'src/pages/page3/main.js',
            template: 'public/index.html',
            filename: 'page3.html'
        },
        page4: {
            entry: 'src/pages/page4/main.js',
            template: 'public/index.html',
            filename: 'page4.html'
        },
        page5: {
            entry: 'src/pages/page5/main.js',
            template: 'public/index.html',
            filename: 'page5.html'
        }
    }
}
