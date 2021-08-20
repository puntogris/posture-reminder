const { description } = require('../../package')

module.exports = {
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#title
   */
  title: 'Posture reminder',
  /**
   * Ref：https://v1.vuepress.vuejs.org/config/#description
   */
  description: 'description',

  /**
   * Extra tags to be injected to the page HTML `<head>`
   *
   * ref：https://v1.vuepress.vuejs.org/config/#head
   */
  head: [
    ['meta', { name: 'theme-color', content: '#3eaf7c' }],
    ['meta', { name: 'apple-mobile-web-app-capable', content: 'yes' }],
    ['meta', { name: 'apple-mobile-web-app-status-bar-style', content: 'black' }]
  ],

  /**
   * Theme configuration, here is the default theme configuration for VuePress.
   *
   * ref：https://v1.vuepress.vuejs.org/theme/default-theme-config.html
   */
  themeConfig: {
    repo: '',
    logo: 'logo.png',
    editLinks: false,
    docsDir: '',
    editLinkText: '',
    lastUpdated: false,
    nav: [
      {
        text: 'Blog',
        link: '/blog/',
      },
      {
        text: 'Contact',
        link: '/contact/'
      },
      {
        text: 'GitHub',
        link: 'https://github.com/puntogris/posture-reminder'
      }
    ],
    sidebar: {
      '/blog/': [
        {
          title: 'Blog',
          collapsable: false,
          children: [
            ''
          ]
        },
        {
          title: 'Versions',
          collapsable: false,
          children: [
            'versions',
            '2.0.0-1a',
          ]
        }
      ],
    }
  },

  /**
   * Apply plugins，ref：https://v1.vuepress.vuejs.org/zh/plugin/
   */
  plugins: [
    '@vuepress/plugin-back-to-top',
    '@vuepress/plugin-medium-zoom',
    '@vuepress/back-to-top'
  ]
}
