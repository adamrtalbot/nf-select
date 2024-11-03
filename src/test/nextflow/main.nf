include { checkInParam } from 'plugin/nf-select'

params.tools = "strelka,star"
params.skip_tools = "manta"

workflow {
    use_strelka    = checkInParam(params.tools, null, false, "|")
    dont_use_manta = checkInParam(params.skip_tools, "manta")
    use_both = checkInParam(params.tools, "strelka") && checkInParam(params.skip_tools, "manta")

    Channel.of(
        use_strelka,
        dont_use_manta,
        use_both
    ) | view
}