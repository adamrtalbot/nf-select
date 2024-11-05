include { select } from 'plugin/nf-select'

params.tools = "strelka"
params.skip_tools = "manta"

workflow {
    use_strelka    = select(pattern: "strelka", select: params.tools)
    dont_use_manta = select(pattern: "manta", antiSelect: params.skip_tools)
    use_both       = select(pattern: "strelka", select: params.tools, antiSelect: params.skip_tools)

    Channel.of(
        use_strelka,
        dont_use_manta,
        use_both
    ) | view
}