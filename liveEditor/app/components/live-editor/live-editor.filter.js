export default function($sce) {
    'ngInject';

    const MAX_HEADING_LEVEL = 6;

    function replaceBold(line) {
        return line.trim().replace(/(\*|__)(.*?)\1/g, '<i>$2</i>');
    }

    function replaceDel(line) {
        return line.trim().replace(/~~(.*?)~~/g, '<del>$1</del>');
    }

    function replaceHeading(line) {
        let headingLevel;
        const match = line.trim().match(new RegExp(`^#{1,${MAX_HEADING_LEVEL}}(?!#)`));

        if (match) {
            headingLevel = match[0].length;
            line = line.replace(new RegExp(`^\\s*#{${headingLevel}}\\s*`), '');

            return `<h${headingLevel}>${line}</h${headingLevel}>`;
        }

        return line;
    }

    function applyMarkdown(input) {
        return input
            .split('\n')
            .map(replaceBold)
            .map(replaceDel)
            .map(replaceHeading)
            .join('\n');
    }

    function replaceNewLines(input) {
        return input.replace(/\n+?/g, '<br>');
    }

    function transformInput(input) {
        return replaceNewLines(applyMarkdown(input));
    }

    return function(input) {
        return $sce.trustAsHtml(transformInput(input));
    }
}